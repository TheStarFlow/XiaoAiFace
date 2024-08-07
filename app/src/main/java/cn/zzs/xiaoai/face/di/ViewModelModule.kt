package cn.zzs.xiaoai.face.di

import cn.zzs.xiaoai.face.MainViewModel
import cn.zzs.xiaoai.face.ui.qrcode.WIFIQrCodeViewModel
import cn.zzs.xiaoai.face.ui.setting.SettingViewModel
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun mainViewModelFactory(factory: MainViewModel.Factory): AssistedViewModelFactory<*, *>


    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    fun settingViewModelFactory(factory: SettingViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(WIFIQrCodeViewModel::class)
    fun qrCodeViewModelFactory(factory: WIFIQrCodeViewModel.Factory): AssistedViewModelFactory<*, *>

}
