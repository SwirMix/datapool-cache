<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="80rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-icon
          medium
          v-bind="attrs"
          v-on="on"
        >
          mdi-information
        </v-icon>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Cache info
        </v-card-title>
        <v-card-text>
           <h3><b>CacheName:</b> {{item.cacheName}}</h3>
           <h3><b>Type:</b> {{item.type}}</h3>
           <h3><b>Status:</b> {{item.status}}</h3>
           <h3><b>Size:</b> {{item.rowCount}}</h3>
           <h3><b>Columns:</b> {{item.columns}}</h3>
           <h3><b>Connection:</b> {{item.connectionProperties}}</h3>
           <h3><b>Consumers:</b> {{item.consumers}}</h3>
           <h3><b>Project:</b> {{item.baseProject}}</h3>
           <v-text-field
            label="Export file"
            v-model="import_file"
           ></v-text-field>
           <v-expansion-panels>
            <v-expansion-panel>
              <v-expansion-panel-header>
                Query
              </v-expansion-panel-header>
              <v-expansion-panel-content>
                <v-container fluid>
                  <v-textarea
                      name="input-7-1"
                      filled
                      label="Datasource query"
                      auto-grow
                      disabled
                      :value="item.query"
                  ></v-textarea>
                </v-container>
              </v-expansion-panel-content>
            </v-expansion-panel>
           </v-expansion-panels>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="success"
            text
            @click="import_request()"
          >
            import cache to file
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="dialog = false"
          >
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
  import apiClient from '@/plugins/backendClient.js'
  import {mapActions} from 'vuex'

  export default {
    props: {
      item: {
        type:Object,
        default(){
            return {}
        }
      }
    },
    data () {
      return {
        dialog: false,
        import_file: ''
      }
    },
    methods: {
      ...mapActions([
         'REFRESH_CACHES'
      ]),
      import_request(){
        apiClient.import_cache_request(this.item, this.import_file)
        this.REFRESH_CACHES(this.item.baseProject)
      }
    }
  }
</script>