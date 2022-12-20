package com.example.effectreturncompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}


@Preview
@Composable
fun Content() {
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

    val context = LocalContext.current

    Column {
        RadioUsePoint()
        RadioNotUsePoint()
        InputUsingPoint()
        CheckUsesAllPoints()

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                Toast.makeText(context, point.toString(), Toast.LENGTH_SHORT).show()
            },
            enabled = isValidInput && isUpdated
        ) {
            Text(text = "Submit!")
        }
    }
}

data class Point(
    val inputPoint: Int,
    val isUsesAllPoint: Boolean,
)

data class Result(
    val RadioUsePoint: @Composable () -> Unit,
    val RadioNotUsePoint: @Composable () -> Unit,
    val CheckUsesAllPoints: @Composable () -> Unit,
    val InputUsingPoint: @Composable () -> Unit,
    val point: Point,
    val isValidInput: Boolean,
    val isUpdated: Boolean,
)

@Composable
fun usePointInput(initialize: Point): Result {
    val (usePoint, setUsePoint) = remember { mutableStateOf(initialize.inputPoint > 0) }
    val (checkedUsesAllPoint, setCheckedUsesAllPoint) = remember { mutableStateOf(initialize.isUsesAllPoint) }
    val (inputPoint, setInputPoint) = remember { mutableStateOf(initialize.inputPoint.toString()) }
    val isValidInput = (usePoint && inputPoint.isNotEmpty()) || !usePoint || checkedUsesAllPoint
    val point = Point(
        inputPoint = if (!usePoint) {
            0
        } else {
            inputPoint.toIntOrNull() ?: 0
        },
        isUsesAllPoint = checkedUsesAllPoint
    )
    val isUpdated =
        initialize.isUsesAllPoint != point.isUsesAllPoint ||
                initialize.inputPoint != point.inputPoint

    val RadioUsePoint = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = usePoint,
                onClick = { setUsePoint(true) },
                enabled = !checkedUsesAllPoint,
                modifier = Modifier.semantics { testTag = "RadioUsePoint" },
            )
            Text(
                text = "使用する",
                style = MaterialTheme.typography.body1.merge(),
            )
        }
    }

    val RadioNotUsePoint = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = !usePoint,
                onClick = { setUsePoint(false) },
                enabled = !checkedUsesAllPoint,
                modifier = Modifier.semantics { testTag = "RadioNotUsePoint" },
            )
            Text(
                text = "使用しない",
                style = MaterialTheme.typography.body1.merge(),
            )
        }
    }

    val CheckUsesAllPoints = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedUsesAllPoint,
                onCheckedChange = { setCheckedUsesAllPoint(!checkedUsesAllPoint) },
                modifier = Modifier.semantics { testTag = "CheckUsesAllPoints" },
            )
            Text(
                text = "すべてのポイントを使用する",
                style = MaterialTheme.typography.body1.merge(),
            )
        }
    }

    val InputUsingPoint = @Composable {
        Box(modifier = Modifier.width(200.dp)) {
            TextField(
                value = inputPoint,
                onValueChange = { setInputPoint(it) },
                label = { Text("使用するポイント") },
                enabled = !checkedUsesAllPoint && usePoint,
                modifier = Modifier.semantics { testTag = "InputUsingPoint" },
            )
        }
    }

    return Result(
        RadioUsePoint = RadioUsePoint,
        RadioNotUsePoint = RadioNotUsePoint,
        CheckUsesAllPoints = CheckUsesAllPoints,
        InputUsingPoint = InputUsingPoint,
        isValidInput = isValidInput,
        isUpdated = isUpdated,
        point = point
    )
}
