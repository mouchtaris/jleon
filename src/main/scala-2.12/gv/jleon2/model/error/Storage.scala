package gv.jleon2
package model
package error

trait Storage extends Any with Handler {
  type Result <: storage.Storage#Request
}
