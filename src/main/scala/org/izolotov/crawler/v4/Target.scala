package org.izolotov.crawler.v4

trait Target[A] {

  def url(): String

  def extract(): Content[A]
//  def extract(): Response[A]
//
//  def process()(implicit processor: Processor[A]): Unit
//
//  def save(): Unit

}
