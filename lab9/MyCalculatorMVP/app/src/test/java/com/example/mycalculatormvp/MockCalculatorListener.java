package com.example.mycalculatormvp;

class MockCalculatorListener implements CalculatorListener {

    int result;

    @Override
    public void onResultCalculated(int result) {
        this.result = result;
    }


}
