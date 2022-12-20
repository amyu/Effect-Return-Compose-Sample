package com.example.effectreturncompose

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class usePointInputTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `全てのポイントを使用するにチェックが入ってるときは､他のRadioはDisable`() {
        // Start the app
        composeTestRule.setContent {
            val (
                RadioUsePoint,
                RadioNotUsePoint,
                CheckUsesAllPoints,
                InputUsingPoint,
                point,
                isValidInput,
                isUpdated,
            ) = usePointInput(
                Point(inputPoint = 300, isUsesAllPoint = false)
            )

            Column {
                RadioUsePoint()
                RadioNotUsePoint()
                CheckUsesAllPoints()
                InputUsingPoint()
            }
        }
        Thread.sleep(1000)

        composeTestRule.onNodeWithTag("CheckUsesAllPoints").performClick()

        composeTestRule.onNodeWithTag("RadioUsePoint").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("RadioNotUsePoint").assertIsNotEnabled()

        Thread.sleep(5000)
    }

    @Test
    fun `ポイントを使用しないを選択している場合はポイント入力エリアをDisableにする`() {
        // Start the app
        composeTestRule.setContent {
            val (
                RadioUsePoint,
                RadioNotUsePoint,
                CheckUsesAllPoints,
                InputUsingPoint,
                point,
                isValidInput,
                isUpdated,
            ) = usePointInput(
                Point(inputPoint = 300, isUsesAllPoint = false)
            )

            Column {
                RadioUsePoint()
                RadioNotUsePoint()
                CheckUsesAllPoints()
                InputUsingPoint()
            }
        }

        Thread.sleep(1000)

        composeTestRule.onNodeWithTag("RadioNotUsePoint").performClick()

        composeTestRule.onNodeWithTag("InputUsingPoint").assertIsNotEnabled()

        Thread.sleep(5000)
    }
}