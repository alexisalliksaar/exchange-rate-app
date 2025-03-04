<script lang="ts">

export interface AppMenuItemLeafNode {
    label: string,
    icon: string,
    to: string
}

</script>
<script setup lang="ts">
import { useLayout } from '@/layout/composables/layout';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const { layoutState, setActiveMenuItem, toggleMenu } = useLayout();

const props = defineProps<{
    item: AppMenuItemLeafNode,
    menuKey: string
}>();

const isActiveMenu = computed<boolean>(() => {
    return layoutState.activeMenuItem === props.menuKey
});


function itemClick() {
    toggleMenu();
    setActiveMenuItem(props.menuKey);
}

function checkActiveRoute(item: AppMenuItemLeafNode): boolean {
    return route.path === item.to;
}
</script>

<template>
    <li :class="{ 'active-menuitem': isActiveMenu }">
        <router-link @click="itemClick()" :class="{ 'active-route': checkActiveRoute(props.item) }" tabindex="0" :to="props.item.to">
            <i :class="props.item.icon" class="layout-menuitem-icon"></i>
            <span class="layout-menuitem-text">{{ props.item.label }}</span>
        </router-link>
    </li>
</template>

<style lang="scss" scoped></style>
