package org.interledger.stream;

import com.google.common.primitives.UnsignedLong;
import org.junit.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class SendMoneyResultTest {

    @Test
    public void testTotalPacketsAndSendMoneyResult() {

        final UnsignedLong originalAmount = UnsignedLong.valueOf(100);
        final UnsignedLong deliveredAmount = UnsignedLong.valueOf(10);
        final Duration moneySendDuration = Duration.ofMillis(1000);

        final int fulfilledPackets = 10;
        final int rejectedPackets = 5;

        SendMoneyResult moneyResult = SendMoneyResult.builder()
                                        .originalAmount(originalAmount)
                                        .amountDelivered(deliveredAmount)
                                        .numFulfilledPackets(fulfilledPackets)
                                        .numRejectPackets(rejectedPackets)
                                        .sendMoneyDuration(moneySendDuration)
                                        .build();

        assertThat(moneyResult.originalAmount()).isEqualTo(UnsignedLong.valueOf(100));
        assertThat(moneyResult.numFulfilledPackets()).isEqualTo(fulfilledPackets);
        assertThat(moneyResult.numRejectPackets()).isEqualTo(rejectedPackets);
        assertThat(moneyResult.amountDelivered()).isEqualTo(deliveredAmount);
        assertThat(moneyResult.totalPackets()).isEqualTo(fulfilledPackets+rejectedPackets);
    }
}
