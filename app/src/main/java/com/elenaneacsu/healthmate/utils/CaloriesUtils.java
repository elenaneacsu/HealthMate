package com.elenaneacsu.healthmate.utils;

import com.elenaneacsu.healthmate.model.User;

public class CaloriesUtils {
    public static long calculateGoalCalories(User user) {
        int goalCals, goalFactor;
        double activityParam;
        switch (user.getActivityLevel()) {
            case "not active":
                activityParam = 1.2;
                break;
            case "lightly active":
                activityParam = 1.375;
                break;
            case "active":
                activityParam = 1.55;
                break;
            case "very active":
                activityParam = 1.725;
                break;
            default:
                activityParam = 1;
                break;
        }
        switch (user.getGoal()) {
            case "lose":
                goalFactor = -350;
                break;
            case "maintain":
                goalFactor = 0;
                break;
            case "gain":
                goalFactor = 300;
                break;
            default:
                goalFactor = 0;
                break;
        }
        if (user.getGender().equalsIgnoreCase("f")) {
            goalCals = (int) ((10 * user.getCurrentWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161) * activityParam) + goalFactor;
        } else {
            goalCals = (int) ((10 * user.getCurrentWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + 5) * activityParam) + goalFactor;
        }

        return goalCals;
    }
}
