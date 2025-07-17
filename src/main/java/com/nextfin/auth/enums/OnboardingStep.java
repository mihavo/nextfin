package com.nextfin.auth.enums;

public enum OnboardingStep {
    EMAIL_VERIFICATION, HOLDER_CREATION, TOS_ACCEPTED, COMPLETED;

    public OnboardingStep next() {
        return switch (this) {
            case EMAIL_VERIFICATION -> HOLDER_CREATION;
            case HOLDER_CREATION -> TOS_ACCEPTED;
            case TOS_ACCEPTED -> COMPLETED;
            case COMPLETED -> null;
        };
    }
}
