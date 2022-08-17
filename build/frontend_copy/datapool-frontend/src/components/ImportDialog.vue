<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="80rem"
    >
      <template v-slot:activator="{ on, attrs }">
          <v-icon
           right
           dark
           v-bind="attrs"
           v-on="on"
          >
           mdi-file-import
          </v-icon>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Import dialog
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
          <v-btn block color="primary" class="ma-4">
            Import data to file. See Storage.
          </v-btn>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="dialog = false"
          >
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
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

    }
  }
</script>