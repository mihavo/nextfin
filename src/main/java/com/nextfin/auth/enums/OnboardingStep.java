package com.nextfin.auth.enums;

public enum OnboardingStep {
    EMAIL_VERIFICATION, HOLDER_CREATION, TOS_ACCEPTANCE, COMPLETED;

    public OnboardingStep next() {
        return switch (this) {
            case EMAIL_VERIFICATION -> HOLDER_CREATION;
            case HOLDER_CREATION -> TOS_ACCEPTANCE;
            case TOS_ACCEPTANCE -> COMPLETED;
            case COMPLETED -> null;
        };
    }
}
