<script setup lang="ts">

const props = withDefaults(defineProps<{
    exRate: string | undefined,
    exRateOptions?: string[]
    selectable?: boolean
}>(), { exRateOptions: () => [], selectable: true })

const modelValue = defineModel<number>({ required: true })
const emit = defineEmits<{
    updateExRate: [value: string | undefined]
}>()

function getCurrencySymbol(currencyCode: string | undefined): string {
    if (!currencyCode) {
        return ""
    }

    const symbol = new Intl.NumberFormat('en', { style: 'currency', currency: currencyCode })
        .formatToParts(1)
        .find(part => part.type === 'currency')?.value
    return  `${symbol} ` || ''
}
</script>
<template>
    <div>
        <div class="mb-2">
            <Select v-if="selectable"
                :modelValue="exRate"
                @update:modelValue="(val) => emit('updateExRate', val)"
                :options="exRateOptions"
                filter
                class ="currency-input"
            />
            <InputText 
                v-else-if="exRate" 
                :value="exRate"
                disabled
                class="un-selectable currency-input"
            />
        </div>
        <div>
            <!-- InputNumber updates its value lazily so use workaround to directly set value on input, see https://github.com/primefaces/primevue/issues/506 -->
            <InputNumber 
                v-model="modelValue"
                @input="modelValue = $event.value as number" 
                :maxFractionDigits="2"
                :allowEmpty=false
                :prefix="getCurrencySymbol(exRate)"
                class="valueInput"
                fluid
            />
        </div>
    </div>
</template>
<style scoped lang="scss">
.un-selectable {
    background: var(--p-inputtext-background) !important;
    color: var(--p-inputtext-color) !important;
}

.currency-input {
    width: 100px;
}
::v-deep(.valueInput > input) {
    font-size: 36px
}
</style>