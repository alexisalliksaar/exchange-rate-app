<script setup lang="ts">
import { ref } from 'vue';

import AppMenuItem, { type AppMenuItemLeafNode } from './AppMenuItem.vue';

const model = ref<{
    label: string,
    items: AppMenuItemLeafNode[]
}[]>([
    {
        label: 'Home',
        items: [
            { label: 'Dashboard', icon: 'pi pi-fw pi-home', to: '/' },
        ]
    },
    {
        label: 'Components',
        items: [
            { label: 'Chart', icon: 'pi pi-fw pi-chart-line', to: '/chart' },
            { label: 'Calculator', icon: 'pi pi-fw pi-euro', to: '/calculator' },
        ]
    },
    
]);
</script>

<template>
    <ul class="layout-menu">
        <div v-for="(items, i) in model" :key="i">
            <li class="layout-root-menuitem">
                <div class="layout-menuitem-root-text">{{ items.label }}</div>
                <ul v-for="(item, j) in items.items" :key="j">
                    <app-menu-item :item="item" :menu-key="`${i}_${j}`"></app-menu-item>
                </ul>
            </li>
        </div>
    </ul>
</template>

<style lang="scss" scoped></style>
