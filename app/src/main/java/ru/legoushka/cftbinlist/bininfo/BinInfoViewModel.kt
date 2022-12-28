package ru.legoushka.cftbinlist.bininfo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.legoushka.cftbinlist.data.models.BinInfo
import ru.legoushka.cftbinlist.data.source.BinInfoRepository
import javax.inject.Inject

@HiltViewModel
class BinInfoViewModel @Inject constructor(
    private val repository: BinInfoRepository
) : ViewModel() {

    val binInfo = mutableStateOf(BinInfo())
    val binTextField = mutableStateOf("45717360")

    fun onGetButtonClick() = viewModelScope.launch{
            binInfo.value = repository.getBinInfo(binTextField.value) ?: BinInfo()
    }
}