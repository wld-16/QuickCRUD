import { createRouter, createWebHistory } from 'vue-router'
import ShipDetails from "@/components/ShipDetails.vue";
import ShipList from "@/components/ShipList.vue";
import ShipCreateForm from "@/components/ShipCreateForm.vue";
import ContainerDetails from "@/components/ContainerDetails.vue";
import ContainerList from "@/components/ContainerList.vue";
import ContainerCreateForm from "@/components/ContainerCreateForm.vue";
import PortDetails from "@/components/PortDetails.vue";
import PortList from "@/components/PortList.vue";
import PortCreateForm from "@/components/PortCreateForm.vue";
import ContainerCraneDetails from "@/components/ContainerCraneDetails.vue";
import ContainerCraneList from "@/components/ContainerCraneList.vue";
import ContainerCraneCreateForm from "@/components/ContainerCraneCreateForm.vue";
import ProductDetails from "@/components/ProductDetails.vue";
import ProductList from "@/components/ProductList.vue";
import ProductCreateForm from "@/components/ProductCreateForm.vue";
import ArticleDetails from "@/components/ArticleDetails.vue";
import ArticleList from "@/components/ArticleList.vue";
import ArticleCreateForm from "@/components/ArticleCreateForm.vue";
import OpportunityDetails from "@/components/OpportunityDetails.vue";
import OpportunityList from "@/components/OpportunityList.vue";
import OpportunityCreateForm from "@/components/OpportunityCreateForm.vue";
import ActivityDetails from "@/components/ActivityDetails.vue";
import ActivityList from "@/components/ActivityList.vue";
import ActivityCreateForm from "@/components/ActivityCreateForm.vue";
import ContactDetails from "@/components/ContactDetails.vue";
import ContactList from "@/components/ContactList.vue";
import ContactCreateForm from "@/components/ContactCreateForm.vue";
import LandingPage from "@/components/LandingPage.vue";
import SpectrumList from "@/components/SpectrumList.vue";
import SpectrumCreateForm from "@/components/SpectrumCreateForm.vue";
import SpectrumDetails from "@/components/SpectrumDetails.vue";

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
		path: '/ship/list',
		name: 'ship-list',
		component: ShipList
	}, {
		path: '/ship/create',
		name: 'ship-create',
		component: ShipCreateForm
	}, 	{
		path: '/container/:id',
		name: 'container-details',
		component: ContainerDetails
	}, {
		path: '/container/list',
		name: 'container-list',
		component: ContainerList
	}, {
		path: '/container/create',
		name: 'container-create',
		component: ContainerCreateForm
	}, 	{
		path: '/port/:id',
		name: 'port-details',
		component: PortDetails
	}, {
		path: '/port/list',
		name: 'port-list',
		component: PortList
	}, {
		path: '/port/create',
		name: 'port-create',
		component: PortCreateForm
	}, 	{
		path: '/containerCrane/:id',
		name: 'containerCrane-details',
		component: ContainerCraneDetails
	}, {
		path: '/containerCrane/list',
		name: 'containerCrane-list',
		component: ContainerCraneList
	}, {
		path: '/containerCrane/create',
		name: 'containerCrane-create',
		component: ContainerCraneCreateForm
	}, 	{
		path: '/product/:id',
		name: 'product-details',
		component: ProductDetails
	}, {
		path: '/product/list',
		name: 'product-list',
		component: ProductList
	}, {
		path: '/product/create',
		name: 'product-create',
		component: ProductCreateForm
	}, 	{
		path: '/article/:id',
		name: 'article-details',
		component: ArticleDetails
	}, {
		path: '/article/list',
		name: 'article-list',
		component: ArticleList
	}, {
		path: '/article/create',
		name: 'article-create',
		component: ArticleCreateForm
	}, 	{
		path: '/opportunity/:id',
		name: 'opportunity-details',
		component: OpportunityDetails
	}, {
		path: '/opportunity/list',
		name: 'opportunity-list',
		component: OpportunityList
	}, {
		path: '/opportunity/create',
		name: 'opportunity-create',
		component: OpportunityCreateForm
	}, 	{
		path: '/activity/:id',
		name: 'activity-details',
		component: ActivityDetails
	}, {
		path: '/activity/list',
		name: 'activity-list',
		component: ActivityList
	}, {
		path: '/activity/create',
		name: 'activity-create',
		component: ActivityCreateForm
	}, 	{
		path: '/contact/:id',
		name: 'contact-details',
		component: ContactDetails
	}, {
		path: '/contact/list',
		name: 'contact-list',
		component: ContactList
	}, {
		path: '/contact/create',
		name: 'contact-create',
		component: ContactCreateForm
	}, {
		path: '/spectrum/list',
		name: 'spectrum-list',
		component: SpectrumList
	},{
		path: '/spectrum/:id',
		name: 'spectrum-details',
		component: SpectrumDetails
	},{
		path: '/spectrum/create',
		name: 'spectrum-create',
		component: SpectrumCreateForm
	}]
const router = createRouter({
	// 4. Provide the history implementation to use. We are using the hash history for simplicity here.
	history: createWebHistory(),
	routes, // short for `routes: routes`
})

export default router