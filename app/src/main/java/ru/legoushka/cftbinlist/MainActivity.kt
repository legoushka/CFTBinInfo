package ru.legoushka.cftbinlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ru.legoushka.cftbinlist.bininfo.BinInfoScreen
import ru.legoushka.cftbinlist.ui.theme.CFTBinListTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CFTBinListTheme {
                BinInfoScreen()
            }
        }
    }
}
