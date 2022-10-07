<template>
  <v-container fluid>
      <v-card class="my-4" flat>
        <v-card-content class="d-flex flex-row-reverse">
          <CreateCacheDialog class="mx-4 my-3" />
          <v-btn
              class="mx-4 my-3"
              fab
              dark
              color="warning"
              @click="reload_all()"
              small
          >
              <v-icon dark>
                mdi-reload
              </v-icon>
          </v-btn>
        </v-card-content>
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
        :items="this.$store.state.caches"
        :items-per-page="10"
        :search="search"
        class="elevation-1"
      >
        <template v-slot:[`item.actions`]="{ item }">
          <v-row>
              <ReloadConfirmDialog class="ma-2" v-bind:item="item"></ReloadConfirmDialog>
              <DeleteDialog class="ma-2" v-bind:item="item"></DeleteDialog>
              <CacheInfoDialog class="ma-2" v-bind:item="item"></CacheInfoDialog>
          </v-row>
        </template>
        <template v-slot:[`item.columns`]="{ item }">
            {{slice_desc(item.columns.join(","))}}
        </template>
        <template v-slot:no-data>
          <v-btn
            color="primary"
          >
            Reset
          </v-btn>
        </template>
      </v-data-table>
  </v-container>
</template>

<script>
  import CacheInfoDialog from '@/components/CacheInfoDialog.vue'
  import DeleteDialog from '@/components/DeleteDialog.vue'
  import ReloadConfirmDialog from '@/components/ReloadConfirmDialog.vue'
  import CreateCacheDialog from '@/components/CreateCacheDialog.vue'
  import {mapGetters} from 'vuex'
  import {mapActions} from 'vuex'
  import apiClient from '@/plugins/backendClient.js'

  export default {
    components: {
      CacheInfoDialog,
      DeleteDialog,
      ReloadConfirmDialog,
      CreateCacheDialog
    },
    data () {
      return {
        search: '',
        headers: [
          {
            text: 'CacheName',
            align: 'start',
            sortable: false,
            value: 'cacheName',
          },
          { text: 'Type', value: 'type' },
          { text: 'project', value: 'baseProject' },
          { text: 'rowCount', value: 'rowCount' },
          { text: 'Status', value: 'status' },
          { text: 'Actions', value: 'actions'}
        ]
      }
    },
    methods: {
        ...mapGetters([
          'GET_CURRENT_PROJECT',
          'GET_CACHES'
        ]),
        ...mapActions([
           'REFRESH_CACHES',
           'REFRESH_CURRENT_PROJECT'
        ]),
        get_caches(){
            this.REFRESH_CACHES(this.GET_CURRENT_PROJECT().id)
            return this.GET_CACHES()
        },
        reload_all(){
            apiClient.post_reload_all(this.GET_CURRENT_PROJECT().id).then((response)=>{
               console.log(response)
               this.REFRESH_CACHES(this.GET_CURRENT_PROJECT().id)
            })
        },
        slice_desc(text){
            var sliced = text.slice(0,50);
            if (sliced.length < text.length) {
                sliced += '...';
            }
            return sliced
        }
    },
    created(){
        this.get_caches()
    }
  }
</script>
