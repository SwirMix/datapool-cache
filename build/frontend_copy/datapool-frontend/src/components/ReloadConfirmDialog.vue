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
          mdi-reload
        </v-icon>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Confirm reload cache
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
            color="primary"
            text
            @click="dialog = false"
          >
            Cancel
          </v-btn>
          <v-btn
            color="warning"
            text
            @click="reload_request()"
          >
            Confirm
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
  import apiClient from '@/plugins/backendClient.js'
  import {mapActions} from 'vuex'
  import {mapGetters} from 'vuex'

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
      }
    },
    methods: {
        ...mapGetters([
          'GET_CURRENT_PROJECT'
        ]),
        ...mapActions([
           'REFRESH_CURRENT_PROJECT'
        ]),
        reload_request(){
            apiClient.post_reload_internal_cache(this.item.cacheName).then((response)=>{
                console.log(response.data)
                this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())
                this.dialog = false
            })
        }
    }
  }
</script>