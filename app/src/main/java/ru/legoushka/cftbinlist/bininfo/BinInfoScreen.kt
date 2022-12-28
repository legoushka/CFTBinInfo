package ru.legoushka.cftbinlist.bininfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun BinInfoScreen(
    viewModel: BinInfoViewModel = hiltViewModel()
) {
    val binInfo = remember {
        viewModel.binInfo
    }

    val history = viewModel.history.collectAsState(initial = listOf())

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = viewModel.binTextField.value,
                label = { Text(text = "BIN банковской карты") },
                placeholder = { Text("1234 5678") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions
                    (onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }),
                onValueChange = { viewModel.binTextField.value = formatBin(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = viewModel::onGetButtonClick) {
                Text(text = "Получить данные")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column() {
                            Text(text = "SCHEME / NETWORK")
                            Text(text = binInfo.value.scheme)
                        }
                        Spacer(modifier = Modifier.width(32.dp))
                        Column() {
                            Text(text = "TYPE")
                            Text(text = binInfo.value.type)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column() {
                            Text(text = "BRAND")
                            Text(text = binInfo.value.brand)
                        }
                        Spacer(modifier = Modifier.width(64.dp))
                        Column() {
                            Text(text = "PREPAID")
                            Text(
                                text = if (binInfo.value.prepaid) {
                                    "Yes"
                                } else {
                                    "No"
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column() {
                            Text(text = "BANK")
                            Text(text = "${binInfo.value.bank.name} ${binInfo.value.bank.city}")
                            Text(text = binInfo.value.bank.url)
                            Text(text = binInfo.value.bank.phone)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column() {
                            Text(text = "CARD NUMBER")
                            Row() {
                                Column() {
                                    Text(text = "Length")
                                    Text(text = binInfo.value.number.length.toString())
                                }
                                Column() {
                                    Text(text = "LUHN")
                                    Text(
                                        text = if (binInfo.value.number.luhn) {
                                            "Yes"
                                        } else {
                                            "No"
                                        }
                                    )
                                }
                            }
                        }
                        Column() {
                            Text(text = "COUNTRY")
                            Text(text = "${binInfo.value.country.emoji} ${binInfo.value.country.name}")
                            Text(text = "(latitude: ${binInfo.value.country.latitude}, longitude: ${binInfo.value.country.longitude})")
                        }
                    }
                }
            }
            Button(onClick = {viewModel.onDeleteHistoryClick()}) {
                Text(text = "Очистить историю")
            }
            LazyColumn {
                items(history.value) { item ->
                    Card(modifier = Modifier.fillMaxWidth(), onClick = {
                        viewModel.onHistoryItemClick(item)
                    }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.bin)
                            Text(text = item.time)
                        }
                    }
                }
            }
        }
    }
}

private fun formatBin(bin: String): String {
    var result = bin
    if (result.length > 8) {
        result = result.take(8)
    }
    return result
}