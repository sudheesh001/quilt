package org.interledger.stream;

import org.interledger.core.Immutable;
import org.interledger.core.InterledgerAddress;
import org.interledger.core.SharedSecret;

import com.google.common.primitives.UnsignedLong;
import org.immutables.value.Value.Check;

import java.time.Duration;
import java.util.Optional;

@Immutable
public interface SendMoneyRequest {

  static SendMoneyRequestBuilder builder() {
    return new SendMoneyRequestBuilder();
  }

  /**
   * The shared secret, shared between only the sender and receiverd, required by IL-RFC-29 to encrypt stream frames so
   * that only the sender and receiver can decrypt them.
   *
   * @return A {@link SharedSecret} known only to the sender and receiver, negotiated using some higher-level protocol
   *     (e.g., SPSP or something else).
   */
  SharedSecret sharedSecret();

  /**
   * The source address of this request.
   *
   * @return The {@link InterledgerAddress} of the source of this payment.
   */
  InterledgerAddress sourceAddress();

  /**
   * The destination address of this request.
   *
   * @return The {@link InterledgerAddress} of the receiver of this payment.
   */
  InterledgerAddress destinationAddress();

  /**
   * The amount of units to send. The denomination of this unit depends on the value of {@link #getSenderAmountMode()}.
   *
   * @return An {@link UnsignedLong} containing the amount of this payment.
   */
  UnsignedLong amount();

  /**
   * Returns the {@link SenderAmountMode} for this payment tracker.
   *
   * @return A {@link SenderAmountMode} that indicates the meaning of {@link #amount()}.
   */
  SenderAmountMode getSenderAmountMode();

  /**
   * The {@link Denomination} of the amount in this request.
   *
   * @return A {@link Denomination}.
   */
  Denomination denomination();

  /**
   * <p>A {@link Duration} to wait before no longer scheduling any more requests to send stream packets for this
   * payment.</p>
   *
   * <p>This is an important distinction as compared to a traditional `hard timeout` where processing might end
   * immediately upon timeout. Instead, this timeout should be considered to be a `soft timeout`, because the sender
   * will wait for any in-flight packetized payments to be sent before exiting out of it run-loop.</p>
   *
   * @return A {@link Duration} of time to wait before terminating this payment.
   */
  Optional<Duration> timeout();

  /**
   * Payment tracker that will monitor payment packets.
   *
   * @return payment tracker
   */
  PaymentTracker<SenderAmountMode> paymentTracker();

  /**
   * Ensures that this request's SenderAmountMode is compatible with {@link PaymentTracker#getOriginalAmountMode()}.
   *
   * @return {@code true} if the SenderAmountMode is compatible with the PaymentTracker; {@code false} otherwise.
   */
  @Check
  default SendMoneyRequest check() {
    if (this.paymentTracker().getOriginalAmountMode() != this.getSenderAmountMode()) {
      throw new IllegalStateException("PaymentTracker is not compatible with this SenderAmountMode");
    }
    return this;
  }
}
