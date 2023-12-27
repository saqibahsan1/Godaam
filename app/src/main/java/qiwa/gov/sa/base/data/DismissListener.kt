package qiwa.gov.sa.base.data

import java.io.Serializable

@FunctionalInterface
interface DismissListener : Serializable {
    fun onDismiss(value: Any? = null)
}

@FunctionalInterface
interface DialogFragDismissalNavArgCallback : Serializable {
    fun dialogDismissed(item: Any? = null)
}

@FunctionalInterface
interface OnBooleanStatusChangeListener : Serializable {
    fun onChanged(changed: Boolean)
}

data class DialogDismissalArgsData(
    @Transient val callback: DialogFragDismissalNavArgCallback
) : Serializable