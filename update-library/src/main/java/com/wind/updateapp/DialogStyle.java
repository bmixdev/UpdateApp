package com.wind.updateapp;

import java.io.Serializable;

/**
 * Created by wind on 16/5/17.
 */
public class DialogStyle implements Serializable{

    private int contentBackground;
    private int leftBtnBackground;
    private int leftBtnTextColor;
    private int rightBtnBackground;
    private int rightBtnTextColor;

    public DialogStyle(int contentBackground, int leftBtnBackground, int leftBtnTextColor, int rightBtnBackground, int rightBtnTextColor) {
        this.contentBackground = contentBackground;
        this.leftBtnBackground = leftBtnBackground;
        this.leftBtnTextColor = leftBtnTextColor;
        this.rightBtnBackground = rightBtnBackground;
        this.rightBtnTextColor = rightBtnTextColor;
    }

    public int getLeftBtnTextColor() {
        return leftBtnTextColor;
    }

    public void setLeftBtnTextColor(int leftBtnTextColor) {
        this.leftBtnTextColor = leftBtnTextColor;
    }

    public int getRightBtnTextColor() {
        return rightBtnTextColor;
    }

    public void setRightBtnTextColor(int rightBtnTextColor) {
        this.rightBtnTextColor = rightBtnTextColor;
    }

    public int getContentBackground() {
        return contentBackground;
    }

    public void setContentBackground(int contentBackground) {
        this.contentBackground = contentBackground;
    }

    public int getLeftBtnBackground() {
        return leftBtnBackground;
    }

    public void setLeftBtnBackground(int leftBtnBackground) {
        this.leftBtnBackground = leftBtnBackground;
    }

    public int getRightBtnBackground() {
        return rightBtnBackground;
    }

    public void setRightBtnBackground(int rightBtnBackground) {
        this.rightBtnBackground = rightBtnBackground;
    }


    public static class Builder{
        private int contentBackground;
        private int leftBtnBackground;
        private int leftBtnTextColor;
        private int rightBtnBackground;
        private int rightBtnTextColor;

        public Builder setContentBackground(int contentBackground) {
            this.contentBackground = contentBackground;
            return this;
        }

        public Builder setLeftBtnBackground(int leftBtnBackground) {
            this.leftBtnBackground = leftBtnBackground;
            return this;
        }

        public Builder setLeftBtnTextColor(int leftBtnTextColor) {
            this.leftBtnTextColor = leftBtnTextColor;
            return this;
        }

        public Builder setRightBtnBackground(int rightBtnBackground) {
            this.rightBtnBackground = rightBtnBackground;
            return this;
        }

        public Builder setRightBtnTextColor(int rightBtnTextColor) {
            this.rightBtnTextColor = rightBtnTextColor;
            return this;
        }
        public DialogStyle build(){
            return new DialogStyle(this.contentBackground,this.leftBtnBackground,this.leftBtnTextColor,this.rightBtnBackground,this.rightBtnTextColor);
        }
    }
}
