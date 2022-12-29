package ru.legoushka.cftbinlist.bininfo

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.legoushka.cftbinlist.data.models.*
import ru.legoushka.cftbinlist.ui.theme.Black
import ru.legoushka.cftbinlist.ui.theme.CFTBinListTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun BinInfoScreen(
    viewModel: BinInfoViewModel = hiltViewModel()
) {
    val binInfo = remember { viewModel.binInfo }
    val binTextField = remember { viewModel.binTextField }

    val history = viewModel.history.collectAsState(initial = listOf())

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        BinInfoContent(
            keyboardController = keyboardController,
            focusManager = focusManager,
            textFieldValue = binTextField.value,
            binInfo = binInfo.value,
            history = history.value,
            onTextFieldValueChange = { viewModel.binTextField.value = it },
            onGetButtonClick = viewModel::onGetButtonClick,
            onUrlClick = { URL ->
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://$URL")
                    )
                )
            },
            onPhoneNumberClick = {tel ->
                context.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:$tel")
                    )
                )
            },
            onCoordinatesClick = { lat, long, country ->
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:$lat,$long?q=$country")
                    )
                )
            },
            onHistoryItemClick = viewModel::onHistoryItemClick,
            onDeleteHistoryClick = viewModel::onDeleteHistoryClick
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BinInfoContent(
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    textFieldValue: String,
    binInfo: BinInfo,
    history: List<BinInfoSearchHistory>,
    onTextFieldValueChange: (String) -> Unit,
    onGetButtonClick: (Context) -> Unit,
    onUrlClick: (String) -> Unit,
    onPhoneNumberClick: (String) -> Unit,
    onCoordinatesClick: (Int, Int, String) -> Unit,
    onHistoryItemClick: (BinInfoSearchHistory) -> Unit,
    onDeleteHistoryClick: () -> Unit,
) {
    val localContext = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = textFieldValue,
            label = {
                Text(
                    text = "BIN банковской карты",
                    style = MaterialTheme.typography.body1,
                    fontSize = 12.sp
                )
            },
            placeholder = { Text("1234 5678 или 1234 56") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions
                (onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            onValueChange = { onTextFieldValueChange(formatBin(it)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onGetButtonClick(localContext) },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)
        ) {
            Text(text = "Получить данные", style = MaterialTheme.typography.body1)
        }
        Spacer(modifier = Modifier.height(8.dp))

        BinInfoCard(binInfo = binInfo, onUrlClick, onPhoneNumberClick, onCoordinatesClick)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onDeleteHistoryClick() },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)
        ) {
            Text(text = "Очистить историю", style = MaterialTheme.typography.body1)
        }
        Spacer(modifier = Modifier.height(8.dp))
        SearchHistory(
            history = history,
            onHistoryItemClick = onHistoryItemClick
        )
    }
}

@Composable
fun BinInfoCard(
    binInfo: BinInfo,
    onUrlClick: (String) -> Unit,
    onPhoneNumberClick: (String) -> Unit,
    onCoordinatesClick: (Int, Int, String) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 1.dp)
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                if (binInfo.scheme != null) {
                    Text(
                        text = binInfo.scheme.replaceFirstChar { it.uppercaseChar() },
                        style = MaterialTheme.typography.h1,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                if (binInfo.type != null) {
                    Text(
                        text = binInfo.type, style = MaterialTheme.typography.h2,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                if (binInfo.brand != null) {
                    Text(
                        text = binInfo.brand, style = MaterialTheme.typography.h2,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
            if (binInfo.bank.name != null) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "${binInfo.bank.name} ${
                        if (binInfo.bank.city != null) {
                            ", ${binInfo.bank.city}"
                        } else {
                            ""
                        }
                    }",
                    style = MaterialTheme.typography.h2
                )
            }
            if (binInfo.bank.url != null) {
                Text(
                    text = binInfo.bank.url,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.clickable { onUrlClick(binInfo.bank.url) })
            }
            if (binInfo.bank.phone != null) {
                Text(text = binInfo.bank.phone, style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.clickable { onPhoneNumberClick(binInfo.bank.phone) })
            }
            if (binInfo.country.name != null) {
                Spacer(modifier = Modifier.height(32.dp))
                Column(modifier = Modifier
                    .clickable {
                        onCoordinatesClick(
                            binInfo.country.latitude!!,
                            binInfo.country.longitude!!,
                            binInfo.country.name
                        )
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(text = binInfo.country.emoji!!)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = binInfo.country.name,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "Coordinates",
                            Modifier.size(20.dp),
                            tint = Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(lat: ${binInfo.country.latitude}, long: ${binInfo.country.longitude})",
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            }
            if (binInfo.number.length != null) {
                Spacer(modifier = Modifier.height(32.dp))
                Row() {
                    Column() {
                        Text(text = "Length", style = MaterialTheme.typography.subtitle1)
                        Text(
                            text = binInfo.number.length.toString(),
                            style = MaterialTheme.typography.body2
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column() {
                        Text(text = "LUHN", style = MaterialTheme.typography.subtitle1)
                        Text(
                            text = when (binInfo.number.luhn) {
                                true -> "yes"
                                false -> "no"
                                else -> "?"
                            }, style = MaterialTheme.typography.body2
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column() {
                        Text(text = "Prepaid", style = MaterialTheme.typography.subtitle1)
                        Text(
                            text = when (binInfo.prepaid) {
                                true -> "yes"
                                false -> "no"
                                else -> "?"
                            }, style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchHistory(
    history: List<BinInfoSearchHistory>,
    onHistoryItemClick: (BinInfoSearchHistory) -> Unit
) {
    LazyColumn {
        items(history) { item ->
            Card(modifier = Modifier.fillMaxWidth(), onClick = {
                onHistoryItemClick(item)
            }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.bin, style = MaterialTheme.typography.body2)
                    Text(text = item.time, style = MaterialTheme.typography.body2)
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

@Preview(showBackground = true)
@Composable
fun PreviewBinInfoCard() {
    CFTBinListTheme() {
        BinInfoCard(
            binInfo = BinInfo(
                bank = Bank(
                    "Hjørring",
                    "Jyske Bank",
                    "+4589893300",
                    "www.jyskebank.dk"
                ),
                "Visa/Dankort",
                Country("DK", "DKK", "\uD83C\uDDE9\uD83C\uDDF0", 56, 10, "Denmark", "208"),
                Number(16, true),
                false,
                "Visa",
                "Debit"
            ),
            onPhoneNumberClick = {},
            onUrlClick = {},
            onCoordinatesClick = { _, _, _ -> }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewBinInfoContent() {
    CFTBinListTheme() {
        Surface(color = MaterialTheme.colors.background) {
            BinInfoContent(
                keyboardController = null,
                focusManager = LocalFocusManager.current,
                textFieldValue = "12345678",
                binInfo = BinInfo(
                    bank = Bank(
                        "Hjørring",
                        "Jyske Bank",
                        "+4589893300",
                        "www.jyskebank.dk"
                    ),
                    "Visa/Dankort",
                    Country("DK", "DKK", "\uD83C\uDDE9\uD83C\uDDF0", 56, 10, "Denmark", "208"),
                    Number(16, true),
                    false,
                    "Visa",
                    "Debit"
                ),
                history = listOf(),
                onTextFieldValueChange = {},
                onGetButtonClick = { },
                onHistoryItemClick = {},
                onDeleteHistoryClick = {},
                onPhoneNumberClick = {},
                onUrlClick = {},
                onCoordinatesClick = { _, _, _ -> }
            )
        }
    }
}