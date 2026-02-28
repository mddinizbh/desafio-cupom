package com.challenge.coupon.presentation.exception;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.CouponNotFoundException;
import com.challenge.coupon.domain.exception.InvalidCouponException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String ERRORS_PROPERTY = "errors";
    private static final String TRACE_ID_PROPERTY = "traceId";
    private static final String PROBLEM_TYPE_PREFIX = "https://yourdomain.com/problems/";

    private final HttpServletRequest request;

    @ExceptionHandler(InvalidCouponException.class)
    public ProblemDetail handleInvalidCoupon(InvalidCouponException ex) {
        return createProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Business Rule Violation",
                ex.getMessage(),
                "invalid-coupon"
        );
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ProblemDetail handleNotFound(CouponNotFoundException ex) {
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                "coupon-not-found"
        );
    }

    @ExceptionHandler(CouponAlreadyDeletedException.class)
    public ProblemDetail handleAlreadyDeleted(CouponAlreadyDeletedException ex) {
        return createProblemDetail(
                HttpStatus.CONFLICT,
                "Conflict",
                ex.getMessage(),
                "coupon-already-deleted"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> java.util.Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"
                ))
                .toList();

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "One or more fields have invalid values",
                "validation-error"
        );
        problemDetail.setProperty(ERRORS_PROPERTY, errors);
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON",
                "Malformed JSON request or invalid field format",
                "malformed-json"
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String detail = String.format("Parameter '%s' has an invalid value", ex.getName());
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid Parameter Type",
                detail,
                "invalid-parameter-type"
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                "internal-server-error"
        );
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, String typeSuffix) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(PROBLEM_TYPE_PREFIX + typeSuffix));
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

}
