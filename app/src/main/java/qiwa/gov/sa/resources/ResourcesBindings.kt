package com.android.wearecovered.resources

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import qiwa.gov.sa.resources.ColorResourceManager
import qiwa.gov.sa.resources.DefaultColorResourceManager
import qiwa.gov.sa.resources.DefaultDrawableResourceManager
import qiwa.gov.sa.resources.DrawableResourceManager

@Module
@InstallIn(SingletonComponent::class)
interface ResourcesBindings {

    @Binds
    fun bindStringsResourceManager(default: DefaultStringsResourceManager): StringsResourceManager

    @Binds
    fun bindDrawableResourceManager(default: DefaultDrawableResourceManager): DrawableResourceManager

    @Binds
    fun bindColorResourceManager(default: DefaultColorResourceManager): ColorResourceManager
}
