package gv
package jleon2
package model.mirror

trait HandlerFactory {

  type Source
  type Handler <: model.mirror.Handler

  def apply(source: Source): util.Try[Handler]

}
