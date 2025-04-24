package com.side.football_project.domain.user.type;

public enum UserTier {
    ROOKIE(0.0),
    BEGINNER(2.0),
    AMATEUR(3.0),
    SEMI_PRO(4.0),
    PRO(5.0);

    private final double minRate;

    UserTier(double minRate) {
        this.minRate = minRate;
    }

    public double getMinRate() {
        return minRate;
    }

    /**
     * 평균 평점(avgRate)을 기반으로 적절한 UserTier 반환
     */
    public static UserTier determineTier(double avgRate) {
        for (UserTier tier : values()) {
            if (avgRate >= tier.getMinRate()) {
                return tier;
            }
        }
        return ROOKIE;
    }
}

