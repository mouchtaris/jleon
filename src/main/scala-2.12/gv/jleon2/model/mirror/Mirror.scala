package gv
package jleon2
package model.mirror

trait Mirror {

  // Inputs
  type Prefix
  type Handler <: model.mirror.Handler {
    type Request = Prefix
  }

  // Definitions
  type HandlerRepository <: isi.Repository {
    type Key >: Prefix
    type Value <: Handler
  }

  type HandlerFactory <: isi.Factory {
    type Construct = Handler
  }

  type HandlerRepositoryFactory <: isi.Factory {
    type Construct <: HandlerRepository
  }
}
