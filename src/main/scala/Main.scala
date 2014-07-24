import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{MapWritable, Text}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.serializer.KryoSerializer
import org.elasticsearch.hadoop.mr.EsInputFormat

object Main {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local").setAppName("Bee-Spark")
    val sc = new SparkContext(conf)
    sc.setLocalProperty("spark.serializer", classOf[KryoSerializer].getName)
    val esconf = new Configuration()
    esconf.set("es.nodes", "vps67962.ovh.net")
    esconf.set("es.port", "9200")
    esconf.set("es.resource", "fta/customer") // index/type
    esconf.set("es.query", "?q=alex")
    val esRDD = sc.newAPIHadoopRDD(esconf, classOf[EsInputFormat[Text, MapWritable]], classOf[Text], classOf[MapWritable])

    val docCount = esRDD.count()
    println(s"Number of hits: $docCount")
    esRDD.foreach(println)
  }
}
