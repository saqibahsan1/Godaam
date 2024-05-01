package com.android.wearecovered.resources

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ballpark.buddy.android.resources.ColorResourceManager
import ballpark.buddy.android.resources.DefaultColorResourceManager
import ballpark.buddy.android.resources.DefaultDrawableResourceManager
import ballpark.buddy.android.resources.DrawableResourceManager

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
