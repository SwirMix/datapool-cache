<template>
  <v-container fluid>
        <v-card class="my-3">
           <v-card-text class="text-right">
             <CreateStaticCacheDialog/>
           </v-card-text>
        </v-card>
        <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
              class="my-2"
        ></v-text-field>
        <v-data-table
            :headers="headers"
            :items="this.GET_STATIC_CACHES()"
            :items-per-page="10"
            :search="search"
            class="elevation-1"
        >
        <template v-slot:[`item.actions`]="{ item }">
          <v-row>
           <v-col>
            <StaticInfoDialog class="ma-3" v-bind:param_item="item"></StaticInfoDialog>
           </v-col>
          </v-row>
        </template>
    </v-data-table>
  </v-container>
</template>

<script>
  import StaticInfoDialog from '@/components/StaticInfoDialog.vue'
  import CreateStaticCacheDialog from '@/components/CreateStaticCacheDialog.vue'
  import {mapActions} from 'vuex'
  import {mapGetters} from 'vuex'

  export default {
    components: {
      StaticInfoDialog,
      CreateStaticCacheDialog
    },
    data: () => ({
        search: '',
        headers: [
          {
            text: 'uuid',
            align: 'start',
            sortable: false,
            value: 'cacheUUID',
          },
          { text: 'projectId', value: 'projectId'},
          { text: 'name', value: 'name' },
          { text: 'last update date', value: 'lastUpdate'},
          { text: '', value: 'actions'}
        ]
    }),
    methods: {
        ...mapGetters([
          'GET_STATIC_CACHES',
          'GET_CURRENT_PROJECT'
        ]),
        ...mapActions([
           'REFRESH_CURRENT_PROJECT'
        ]),
    }
  }
</script>