package com.vrixel.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class IdentifiersTest {

    @Test
    void acceptsPrefixedUlids() {
        assertThat(Identifiers.isOpaque("pat_01J9Z3M8Q4R7T2V5W8X1Y3Z6A9")).isTrue();
        assertThat(Identifiers.requireOpaque("enc_01J9Z3M8Q4R7T2V5W8X1Y3Z6A9")).isEqualTo("enc_01J9Z3M8Q4R7T2V5W8X1Y3Z6A9");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"MRN-12345", "pat_123", "PAT_01J9Z3M8Q4R7T2V5W8X1Y3Z6A9", "pat_01J9Z3M8Q4R7T2V5W8X1Y3Z6AI"})
    void rejectsAnythingElse(String id) {
        assertThat(Identifiers.isOpaque(id)).isFalse();
    }

    @Test
    void neverEchoesTheRejectedValue() {
        assertThatThrownBy(() -> Identifiers.requireOpaque("Jane Doe 1980-01-01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageNotContaining("Jane");
    }
}
