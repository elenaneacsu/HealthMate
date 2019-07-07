//package com.elenaneacsu.healthmate.model;
//
//import com.elenaneacsu.healthmate.utils.TimeUtils;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class RegisteredDay {
//    private Date date;
//    private double goalCals;
//    private double totalCals;
//    private List<Meal> meals = new ArrayList<>();
//
//    public RegisteredDay(Date date) {
//        this.date = TimeUtils.getDateWithNoTime(date);
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public double getGoalCals() {
//        return goalCals;
//    }
//
//    public void setGoalCals(double goalCals) {
//        this.goalCals = goalCals;
//    }
//
//    public double getTotalCals() {
//        calculateTotalCalories();
//        return totalCals;
//    }
//
//    public void setTotalCals(double totalCals) {
//        this.totalCals = totalCals;
//    }
//
//    public List<Meal> getMeals() {
//        return meals;
//    }
//
//    public void setMeals(List<Meal> meals) {
//        this.meals = meals;
//        calculateTotalCalories();
//    }
//
//    private void calculateTotalCalories() {
//        double total = 0;
//        for (Meal meal : meals) {
//            total += meal.getCalories();
//        }
//        totalCals = total;
//    }
//
//    public void addMead(Meal meal) {
//        if (!meals.contains(meal)) {
//            meals.add(meal);
//        }
//    }
//
//    public void removeMeal(Meal meal) {
//        if (meals.contains(meal)) {
//            meals.remove(meal);
//        }
//    }
//
//    public Meal getMeal(String title) {
//        Meal meal = new Meal(title, new ArrayList<Food>());
//        if (meals.contains(meal)) {
//            int index = meals.indexOf(meal);
//            return meals.get(index);
//        }
//        return null;
//    }
//
//}
