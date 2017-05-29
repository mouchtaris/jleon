package gv.jleon2
package model
package error

trait Mirror extends Any with Handler {
  type Result <: mirror.Mirror#Handler
}
