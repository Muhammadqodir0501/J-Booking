package com.example.jbooking.service.impl;

import com.example.jbooking.client.JBankClient;
import com.example.jbooking.client.JNotificationClient;
import com.example.jbooking.entity.Booking;
import com.example.jbooking.enums.BookingStatus;
import com.example.jbooking.repository.BookingRepository;
import com.example.jbooking.service.abstaction.PaymentService;
import com.example.jbooking.dto.request.jbank.TransactionRequestDto;
import com.example.jbooking.dto.response.jbank.TransactionResponseDto;
import com.example.jbooking.entity.Payment;
import com.example.jbooking.enums.PaymentStatus;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final JBankClient jBankClient;
    private final BookingRepository bookingRepository;
    private final JNotificationClient jNotificationClient;

    @Value("${jbank.merchant-id:3fa85f64-5717-4562-b3fc-2c963f66afa6}")
    private UUID merchantId;

    @Value("${jbank.receiver-token}")
    private String platformReceiverToken;

    @Transactional
    @Override
    public Payment initiatePayment(UUID bookingId, BigDecimal amount, String senderToken) {
        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .amount(amount)
                .currency("UZS")
                .status(PaymentStatus.PENDING)
                .build();
        payment = paymentRepository.save(payment);

        TransactionRequestDto bankRequest = TransactionRequestDto.builder()
                .referenceId(payment.getId())
                .type("P2P")
                .amount(amount)
                .currency("UZS")
                .merchantId(merchantId)
                .senderToken(senderToken)
                .receiverToken(platformReceiverToken)
                .build();

        TransactionResponseDto bankResponse = jBankClient.createTransaction(bankRequest);

        payment.setBankTransactionId(bankResponse.getId());
        return paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public void handleBankWebhook(TransactionResponseDto webhookPayload) {
        log.info("Получен вебхук от J-Bank для транзакции: {}", webhookPayload.getId());

        Payment payment = paymentRepository.findByBankTransactionId(webhookPayload.getId())
                .orElseThrow(() -> new NotFoundException("Платеж с bankTransactionId " + webhookPayload.getId() + " не найден"));

        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        if ("SUCCESS".equalsIgnoreCase(webhookPayload.getStatus()) || "COMPLETED".equalsIgnoreCase(webhookPayload.getStatus())) {

            payment.setStatus(PaymentStatus.SUCCESS);
            booking.setStatus(BookingStatus.PAID);

            log.info("Платеж {} успешен. Бронь {} переведена в статус PAID.", payment.getId(), booking.getId());
            String message = String.format("Уважаемый клиент! Ваша бронь #%s успешно оплачена. Сумма: %s UZS.",
                    booking.getId(), payment.getAmount());
            jNotificationClient.sendEmail(booking.getUser().getEmail(), message);

        } else if ("FAILED".equalsIgnoreCase(webhookPayload.getStatus()) || "REJECTED".equalsIgnoreCase(webhookPayload.getStatus())) {

            payment.setStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);

            String errorMessage = String.format("Ошибка оплаты! Транзакция для брони #%s отклонена. Бронь аннулирована.",
                    booking.getId());
            jNotificationClient.sendEmail(booking.getUser().getEmail(), errorMessage);

            log.error("Платеж {} отклонен банком. Бронь {} отменена.", payment.getId(), booking.getId());

        } else {
            log.warn("Получен неизвестный статус платежа: {}", webhookPayload.getStatus());
        }

        paymentRepository.save(payment);
        bookingRepository.save(booking);
    }
}
