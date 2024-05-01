package ballpark.buddy.android.base.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import ballpark.buddy.android.base.data.ViewState

interface FlowUseCase<in P, R> {
    operator fun invoke(parameters: P): Flow<ViewState<R>> =
        execute(parameters).flowOn(dispatcher())
    fun execute(parameters: P): Flow<ViewState<R>>
    fun dispatcher(): CoroutineDispatcher
}
