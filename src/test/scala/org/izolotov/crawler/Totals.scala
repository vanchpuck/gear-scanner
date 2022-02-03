//package org.izolotov.crawler
//
//import org.apache.spark.sql.{SaveMode, SparkSession}
//import org.scalatest.FlatSpec
//
//class Totals extends FlatSpec {
//
//  it should "..." in {
//    val spark = SparkSession
//      .builder
//      .master("local[*]") //This is the key config change to make it a local in process spark session.
//      .appName("myApp")
//      .getOrCreate()
//    import spark.implicits._
//    spark.read.option("header", "true").option("encoding", "utf-8").option("delimiter","\t")
//      .csv(s"/home/izolotov/Documents/Totals-1.csv")
////      .createOrReplaceGlobalTempView("data")
////    spark.sql(
////      s"""
////         |select PartnerID, PartnerName from global_temp.data
////         |""".stripMargin).show()
//      .filter($"PartnerID" === 134 || $"PartnerID" === 156 || $"PartnerID" === 310|| $"PartnerID" === 350|| $"PartnerID" === 411)
//      .select("`PartnerID`", "`PartnerName`", "`ScoreGroup`", "`P1Balance of Accounts`", "`P1No. of Accounts`", "`P1Own Cu Balance of Accounts`", "`P1Own Cu No. of Accounts`")
//      .orderBy($"PartnerID", $"ScoreGroup")
//      .repartition(1)
//      .write.mode(SaveMode.Overwrite).option("header", "true")
//      .csv(s"/home/izolotov/Documents/SF_Totals/")
//  }
//
//}
