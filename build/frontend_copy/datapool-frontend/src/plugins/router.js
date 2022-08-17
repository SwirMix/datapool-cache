import DatapoolCaches from "../pages/DatapoolCaches.vue";
import NodeCluster from "../pages/NodeCluster.vue";
import DatapoolStorage from "../pages/DatapoolStorage.vue"
import StaticParameters from "../pages/StaticParameters.vue"
import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);
const routes = [
  {
    path: "/",
    name: "DatapoolCaches",
    component: DatapoolCaches,
  },
  {
    path: "/cluster",
    name: "NodeCluster",
    component: NodeCluster,
  },
  {
    path: "/storage",
    name: "DatapoolStorage",
    component: DatapoolStorage,
  },
  {
    path: "/params",
    name: "StaticParameters",
    component: StaticParameters,
  }
];


const router = new VueRouter({
  mode: "history",
  routes,
});

export default router;