<template>
  <v-container fluid>
      <v-data-table
        :headers="headers"
        :items="cluster"
        :items-per-page="20"
        :search="search"
        class="elevation-1"
      >
      <template v-slot:[`cluster.NodeId`]="{ cluster }">
        {{cluster.attributes}}
      </template>
      </v-data-table>
  </v-container>
</template>

<script>
  import apiClient from '@/plugins/backendClient.js'

  export default {
    data () {
      return {
        search: '',
        headers: [
          {
            text: 'NodeId',
            align: 'start',
            sortable: false,
            value: 'nodeId',
          },
          { text: 'instanceName', value: 'instanceName'},
          { text: 'port', value: 'port'},
          { text: 'addresses', value: 'ips' },
          { text: 'cpu', value: 'cores' }
        ],
        cluster: [],
      }
    },
    methods: {
    },
    created(){
        apiClient.get_cluster_info().then((response)=>{
           this.cluster = response.data
        })
    },
    mounted(){
        apiClient.get_cluster_info().then((response)=>{
           this.cluster = response.data
        })
    }
  }
</script>
