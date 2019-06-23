package com.elenaneacsu.healthmate.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Meal extends ExpandableGroup<Food> {
    private double calories;
    private long id;

    public Meal(String title, List<Food> items) {
        super(title, items);
        getTotalCalories();
    }

    private void getTotalCalories() {
        double totalCalories = 0;
        for(Food food : getItems()) {
            totalCalories+= food.getNutrients().getENER_KCAL();
        }
        calories = totalCalories;
    }

    public double getCalories() {
        getTotalCalories();
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void addFood(Food food) {
        List<Food> foodList = getItems();
        foodList.add(food);
    }

    public void removeFood(Food food) {
        getItems().remove(food);
    }

    public void removeFood(int index) {
        getItems().remove(index);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
