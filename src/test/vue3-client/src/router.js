import { createRouter, createWebHistory } from 'vue-router'
import ShipDetails from "@/components/ShipDetails.vue";
import ShipList from "@/components/ShipList.vue";
import ContainerDetails from "@/components/ContainerDetails.vue";
import ContainerList from "@/components/ContainerList.vue";
import PortDetails from "@/components/PortDetails.vue";
import PortList from "@/components/PortList.vue";
import ContainerCraneDetails from "@/components/ContainerCraneDetails.vue";
import ContainerCraneList from "@/components/ContainerCraneList.vue";
import ProductDetails from "@/components/ProductDetails.vue";
import ProductList from "@/components/ProductList.vue";
import ArticleDetails from "@/components/ArticleDetails.vue";
import ArticleList from "@/components/ArticleList.vue";
import OpportunityDetails from "@/components/OpportunityDetails.vue";
import OpportunityList from "@/components/OpportunityList.vue";
import ActivityDetails from "@/components/ActivityDetails.vue";
import ActivityList from "@/components/ActivityList.vue";
import ContactDetails from "@/components/ContactDetails.vue";
import ContactList from "@/components/ContactList.vue";
import LandingPage from "@/components/LandingPage.vue";

const routes = [
	{
		path: '/',
		name: 'landing-page',
		component: LandingPage
	},
{
		path: '/ship/:id',
		name: 'ship-details',
		component: ShipDetails
	}, {
		path: '/ship-list',
		name: 'ship-list',
		component: ShipList
	}, {
		path: '/container/:id',
		name: 'container-details',
		component: ContainerDetails
	}, {
		path: '/container-list',
		name: 'container-list',
		component: ContainerList
	}, {
		path: '/port/:id',
		name: 'port-details',
		component: PortDetails
	}, {
		path: '/port-list',
		name: 'port-list',
		component: PortList
	}, {
		path: '/containerCrane/:id',
		name: 'containerCrane-details',
		component: ContainerCraneDetails
	}, {
		path: '/containerCrane-list',
		name: 'containerCrane-list',
		component: ContainerCraneList
	}, {
		path: '/product/:id',
		name: 'product-details',
		component: ProductDetails
	}, {
		path: '/product-list',
		name: 'product-list',
		component: ProductList
	}, {
		path: '/article/:id',
		name: 'article-details',
		component: ArticleDetails
	}, {
		path: '/article-list',
		name: 'article-list',
		component: ArticleList
	}, {
		path: '/opportunity/:id',
		name: 'opportunity-details',
		component: OpportunityDetails
	}, {
		path: '/opportunity-list',
		name: 'opportunity-list',
		component: OpportunityList
	}, {
		path: '/activity/:id',
		name: 'activity-details',
		component: ActivityDetails
	}, {
		path: '/activity-list',
		name: 'activity-list',
		component: ActivityList
	}, {
		path: '/contact/:id',
		name: 'contact-details',
		component: ContactDetails
	}, {
		path: '/contact-list',
		name: 'contact-list',
		component: ContactList
	}]
const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
	history: createWebHistory(),
	routes, // short for `routes: routes`
})

export default router