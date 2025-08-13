package com.nextfin.auth.enums;

public enum OnboardingStep {
    HOLDER_CREATION, TOS_ACCEPTANCE, EMAIL_VERIFICATION, COMPLETED;

    public OnboardingStep next() {
        return switch (this) {
            case EMAIL_VERIFICATION -> HOLDER_CREATION;
            case HOLDER_CREATION -> TOS_ACCEPTANCE;
            case TOS_ACCEPTANCE -> EMAIL_VERIFICATION;
            case COMPLETED -> null;
        };
    }
}
