package qiwa.gov.sa.base.domain

interface Mapper<F, T> {
    fun map(from: F): T
}