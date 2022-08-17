<template>
  <v-container fluid>
      <v-card class="my-4" flat>
        <v-card-content class="d-flex flex-row-reverse">
            <v-btn
              class="ma-4"
              fab
              dark
              color="indigo"
              @click="upload_file_request()"
            >
              <v-icon dark>
                mdi-plus
              </v-icon>
            </v-btn>
            <v-file-input
              class="ma-4"
              truncate-length="15"
              v-model="upload_file"
            ></v-file-input>
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
        :items="this.GET_STORAGE()"
        :items-per-page="10"
        :search="search"
        class="elevation-1 text-right"
      >
        <template v-slot:[`item.actions`]="{ item }">
          <div class="d-flex justify-end">
            <StorageImportDialog v-bind:item="item" class="mr-4"/>
            <DeleteStorageFileDialog :item="item"/>
            <v-btn
              color="white"
              outlined
              class="ma-2 white--text"
              :href="download_url(item)"
            >
              Download
              <v-icon
                right
                dark
              >
                mdi-download-circle
              </v-icon>
            </v-btn>
          </div>
        </template>
      </v-data-table>
  </v-container>
</template>

<script>
  import DeleteStorageFileDialog from '@/components/DeleteStorageFileDialog.vue'
  import StorageImportDialog from '@/components/StorageImportDialog.vue'
  import {mapGetters} from 'vuex'
  import apiClient from '@/plugins/backendClient.js'
  import {mapActions} from 'vuex'

  export default {
    components: {
        DeleteStorageFileDialog,
        StorageImportDialog
    },
    data () {
      return {
        upload_file: {},
        search: '',
        headers: [
          {
            text: 'fileName',
            align: 'start',
            sortable: false,
            value: 'name',
          },
          { text: 'size', value: 'size' },
          { text: '', value: 'actions'}
        ],
      }
    },
    methods: {
        ...mapGetters([
          'GET_STORAGE',
          'GET_CURRENT_PROJECT'
        ]),
        ...mapActions([
           'REFRESH_CURRENT_PROJECT'
        ]),
        upload_file_request(){
            console.log(this.upload_file)
            let data = new FormData()
            data.append('file', this.upload_file)
            apiClient.post_upload_file(data, this.GET_CURRENT_PROJECT().id).then((response)=>{
               console.log(response.data)
               this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())
            });
        },
       download_url(item){
            return apiClient.server_endpoint + item.download
       },
    }
  }
</script>