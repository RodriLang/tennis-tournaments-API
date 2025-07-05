package dev.rodrilang.tennis_tournaments.exceptions.handler;

import dev.rodrilang.tennis_tournaments.controllers.PlayerController;
import dev.rodrilang.tennis_tournaments.exceptions.*;
import dev.rodrilang.tennis_tournaments.exceptions.responses.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Error de entidad no encontrada en la base de datos
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.NOT_FOUND, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTournamentStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTournamentStatus(InvalidTournamentStatusException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.CONFLICT, request), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidResultException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResult(InvalidResultException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.BAD_REQUEST, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatePlayerException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePlayer(DuplicatePlayerException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.CONFLICT, request), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DeletedPlayerException.class)
    public ResponseEntity<ErrorResponse> handleDeletedPlayer(DeletedPlayerException e, HttpServletRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(e, HttpStatus.CONFLICT, request);

       errorResponse.add(linkTo(methodOn(PlayerController.class)
                .restorePlayer(e.getPlayerDni()))
                .withRel("restore"));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncompleteMatchException.class)
    public ResponseEntity<ErrorResponse> handleIncompleteMatch(IncompleteMatchException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.CONFLICT, request), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CredentialNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCredentialNotFound(CredentialNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request), HttpStatus.UNAUTHORIZED);
    }

    // Errores de validación de DTO (@Valid fallidos)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.BAD_REQUEST, request), HttpStatus.BAD_REQUEST);
    }

    // JSON mal formado o tipo de datos incompatible en el request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.BAD_REQUEST, request), HttpStatus.BAD_REQUEST);
    }

    // IllegalArgument o IllegalState: cuando el flujo de la lógica falla
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgument(RuntimeException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.BAD_REQUEST, request), HttpStatus.BAD_REQUEST);
    }

    // NullPointer: bug interno
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(NullPointerException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Catch-All: para cualquier otra excepción no controlada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Error de acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(e, HttpStatus.FORBIDDEN, request), HttpStatus.FORBIDDEN);
    }

    private ErrorResponse buildErrorResponse(Exception e, HttpStatus status, HttpServletRequest request) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );
    }
}