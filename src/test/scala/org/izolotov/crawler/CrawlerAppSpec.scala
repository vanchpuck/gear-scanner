package org.izolotov.crawler

import org.izolotov.crawler.parser.origin.OriginCategory
import org.scalatest.flatspec.AnyFlatSpec
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

class CrawlerAppSpec extends AnyFlatSpec {

  it should "run the whole crawler cycle for category" in {
    implicit val Formats = org.json4s.DefaultFormats
    val queue = new SQSQueue[CrawlQueueRecord](SqsClient.builder.region(Region.US_EAST_2).build, "CrawlQueue")
//    queue.add(CrawlQueueRecord("https://tramontana.ru/catalog/termosy_1/", "category"))
//    queue.add(CrawlQueueRecord("sport-marafon.ru/catalog/alpinistskie-kaski/", "category"))
//    queue.add(CrawlQueueRecord("https://tramontana.ru/catalog/koshki_2/", "category"))
//    queue.add(CrawlQueueRecord("https://www.rei.com/product/130561/camp-usa-stalker-universal-crampons", "product"))
//    queue.add(CrawlQueueRecord("https://tramontana.ru/product/petzl_koshki_sarken/", "product"))
//    queue.add((CrawlQueueRecord("https://grivel.com/collections/crampons", OriginCategory.Kind)))
//    queue.add(CrawlQueueRecord("https://tramontana.ru/brands/petzl/ledovoe_snaryazhenie/", "category"))
//    queue.add(CrawlQueueRecord("https://tramontana.ru/catalog/koshki_2/?SECTION_CODE=koshki_2&set_filter=y&arrFilter_2_740967158=Y", "category"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy.grivel/", "category"))
//    queue.add(CrawlQueueRecord("https://www.backcountry.com/ice-climbing-crampons?p=Brand%3A100000010&nf=1", "category"))
//    queue.add(CrawlQueueRecord("https://sport-marafon.ru/catalog/alpinistskie-koshki/?brand-name%5B0%5D=Grivel&gclid=CjwKCAiAo5qABhBdEiwAOtGmbp_EOpCDemEGGypubE0kZDXhZ2kx58VI-Ju_WAgRsRb0OmXPbhSUExoCRb0QAvD_BwE", "category"))
//    queue.add(CrawlQueueRecord("https://www.rei.com/b/grivel/c/crampons", "category"))

//    queue.add((CrawlQueueRecord("https://grivel.com/collections/crampons", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://blackdiamondequipment.ru/catalog/ice-tools", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://petzl.ru/sport/crampons", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://www.edelrid.de/en/sports/icegear.htm", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://www.singingrock.com/crampons-classic", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://www.singingrock.com/crampons-technical", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://www.singingrock.com/crampons-accessories", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://www.camp-usa.com/outdoor/products/crampons/", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://www.salewa.com/climbing-crampons", OriginCategory.Kind)))
//    queue.add((CrawlQueueRecord("https://www.climbingtechnology.com/en/outdoor-en/ice-axes-and-crampons", OriginCategory.Kind)))

    queue.add(CrawlQueueRecord("https://sport-marafon.ru/catalog/alpinistskie-koshki/", "category"))
    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/koshki/", "category"))
    queue.add(CrawlQueueRecord("https://tramontana.ru/catalog/koshki_2/", "category"))
    queue.add(CrawlQueueRecord("https://www.backcountry.com/mountaineering-crampons", "category"))
    queue.add(CrawlQueueRecord("https://www.rei.com/c/crampons", "category"))
    queue.add(CrawlQueueRecord("https://www.kant.ru/catalog/mountaineering/crampons/", "category"))
    queue.add(CrawlQueueRecord("https://www.densurka.ru/products/151", "category"))
    queue.add(CrawlQueueRecord("https://www.trekkinn.com/%D0%B3%D0%BE%D1%80%D0%BD%D1%8B%D0%B5/%D0%A2%D0%BE%D0%B2%D0%B0%D1%80%D1%8B-%D0%B4%D0%BB%D1%8F-%D1%85%D0%BE%D0%B4%D1%8C%D0%B1%D1%8B-%D0%9A%D0%BE%D1%88%D0%BA%D0%B8/14469/s", "category"))

//    queue.add(CrawlQueueRecord("https://tramontana.ru/product/black_diamond_koshki_cyborg_pro/", "product"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-17650/", "product"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-9446/", "product"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-37236/", "product"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/21569/", "product"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-15135/", "product"))

//    queue.add(CrawlQueueRecord("https://www.petzl.com/US/en/Sport/Crampons", OriginCategory.Kind))
//    queue.add((CrawlQueueRecord("https://grivel.com/collections/crampons", OriginCategory.Kind)))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/?brands%5B%5D=151&nal%5B%5D=-1&nal%5B%5D=49&nal%5B%5D=93&div=catalog&num_on_page=20&page=0&cat=alpinistskoe-snaryajenie&subcat=koshki-snegostupy&orderby=", "category"))
//    queue.add(CrawlQueueRecord("https://tramontana.ru/catalog/koshki_2/?SECTION_CODE=koshki_2&set_filter=y&arrFilter_2_628864752=Y", "category"))
//    queue.add(CrawlQueueRecord("https://www.backcountry.com/ice-climbing-crampons?p=Brand%3A55&nf=1", "category"))
//    queue.add(CrawlQueueRecord("https://www.trekkinn.com/%D0%B3%D0%BE%D1%80%D0%BD%D1%8B%D0%B5/%D0%A2%D0%BE%D0%B2%D0%B0%D1%80%D1%8B-%D0%B4%D0%BB%D1%8F-%D1%85%D0%BE%D0%B4%D1%8C%D0%B1%D1%8B-%D0%9A%D0%BE%D1%88%D0%BA%D0%B8/14469/s#fq=id_familia%3A11242&fq={!tag=ds}id_subfamilia%3A14469&fq=id_tienda%3A3&sort=v30+desc,product(tm3,%20dispo)+asc&&start=0&fq={!tag=dm}marca%3A%22Petzl%22", "category"))
//    queue.add(CrawlQueueRecord("https://sport-marafon.ru/catalog/alpinistskie-koshki/?brand-name%5B%5D=Petzl", "category"))

    CrawlerApp.main(Array(
      "--user-agent", "PodBot/0.1",
      "--delay", "4000",
      "--timeout", "10000",
      "--sqs-max-miss-count", "3",
      "--aws-region", "us-east-2",
      "--sqs-classifier-queue-name", "ProductClassifierQueue",
//      "--sqs-processing-queue-name", "ProcessingQueue",
      "--sqs-queue-name", "CrawlQueue",
      "--sqs-dl-queue-name", "DeadLetterCrawlQueue",
      "--sqs-wait-time", "2",
      "--crawl-table", "GearScannerTest",
      "--image-bucket-arn", "arn:aws:s3:us-east-2:848625190772:accesspoint/internet"
    ))
  }

}
