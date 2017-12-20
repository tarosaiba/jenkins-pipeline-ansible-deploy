//  Name              :     deploy_file.groovy
//  Description       :     任意のファイルをリモートホストへ転送します
//                    :     ansibleのcopyモジュールを使用します
//  Parameter         :     REMOTE_HOST: リモートホスト(ansible hostsで定義)
//  Artifact          :     N/A
//  Return            :     N/A
//
//  Upper JOB         :     N/A
//  Next  JOB         :     N/A
//
//  Update History
//          Ver             Date            Name                  Comment
//          1.0             2017/12/20      Saiba Shuntaro        Initial Release

// ジョブパラメータ設定
properties([
  [$class: 'ParametersDefinitionProperty',
    parameterDefinitions: [
      [$class: 'StringParameterDefinition',
        name: 'REMOTE_HOST',
        //defaultValue: 'some value', デフォルト値も設定可能だが、今回は使用しない
        description: '環境識別子[prd,stg,dev]']]
  ]
])

// Define
def remote_host = "${env.REMOTE_HOST}" // get Jenkins Parameter
def deploy_file = "./deploy_files/test.txt"
def remote_path = "/tmp/"

// for git clone
def credentialsId = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" // Jenkinsで使用可能なCredential
def git_repo_jenkins = "jenkins-pipeline-ansible-deploy"
def git_url_jenkins = "https://github.com/tarosaiba/${git_repo_jenkins}.git" // 自環境に合わせて設定

// for get deploy file
// def wget_url = "http://xx.xx.xx.xx:8080/job/hoge-job/lastSuccessfulBuild/artifact/fuga-artifact.tar.gz"


// Pipeline
node {
   stage('Workspase') {
      // Workspace有効化
      ws('workspace') {
      }
   }
   stage('GitClone') {
      // Jenkins repoをclone
      git credentialsId: "${credentialsId}", url: "${git_url_jenkins}"
   }
   stage('GetDeployFile') {
      // 転送用ファイルの取得
      // 本サンプルは"./deploy_files/test.txt"を使用するため何もしない
      sh ":"
      //// Sample 以下のようにJenkinsの成果物をwgetで取得することも可能
      //// sh "wget ${wget_url}  -P ./ansible/deploy_files"
   }
   stage('DeployFileToRemotehost') {
      // 指定ファイルの転送
      sh "cd ./ansible && ansible-playbook deploy_local_file.yml --extra-vars \"host=${remote_host} local_path=${deploy_file} remote_path=${remote_path}\""
   }
}
