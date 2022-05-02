package com.example.tennisball

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.drawable.shapes.Shape
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tennisball.ui.theme.TennisballTheme
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    val text = mutableStateOf("")
    var sm: SensorManager? = null
    var list: List<Sensor>? = null
    var values = mutableStateOf(FloatArray(3) { 0f })
    var x = mutableStateOf(150f)
    var y = mutableStateOf(300f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        list = sm!!.getSensorList(Sensor.TYPE_ACCELEROMETER)
        if (list!!.size > 0) {
            sm!!.registerListener(sel, list!![0], SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(baseContext, "Error: No Accelerometer.", Toast.LENGTH_LONG).show()
        }
        setContent {
            TennisballTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Text(text = text.value)
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.ball),
                            contentDescription = "ball",
                            modifier = Modifier
                                .offset(x.value.dp, y.value.dp)
//                                .offset(x = (150.dp + -(values.value[0].dp * 100)), y = 300.dp + values.value[1].dp * 100)
                                .size(50.dp, 50.dp)
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (list!!.count() > 0) {
            sm!!.unregisterListener(sel);
        }
    }

    var sel: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent) {
//            val values = event.values
            values.value = event.values
            x.value = x.value + (-(event.values[0]) * 5)
            y.value = y.value + ((event.values[1]) * 5)
            text.value = (
                    """
                x: ${values.value[0]}
                y: ${values.value[1]}
                z: ${values.value[2]}
                """.trimIndent()
                    )
        }
    }
}