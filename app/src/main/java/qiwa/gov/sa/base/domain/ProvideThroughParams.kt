package qiwa.gov.sa.base.domain

interface ProvideThroughParams<Params, ReturnType> {
    fun get(params: Params): ReturnType
}
