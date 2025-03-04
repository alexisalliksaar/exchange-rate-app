import AppLayout from '@/layout/AppLayout.vue';
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: AppLayout,
            children: [
                {
                    path: '/',
                    name: 'dashboard',
                    component: () => import('@/views/Dashboard.vue')
                },
                {
                    path: '/calculator',
                    name: 'calculator',
                    component: () => import('@/views/CalculatorView.vue')
                },
                {
                    path: '/chart',
                    name: 'chart',
                    component: () => import('@/views/ChartsView.vue')
                },
            ]
        },
    ]
});

export default router;
