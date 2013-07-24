package controllers

import org.joda.time.DateTime

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

import play.api._
import play.api.mvc._
import play.api.Play.current

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.modules.reactivemongo.json.collection.JSONCollection
import play.autosource.reactivemongo._
import play.api.libs.ws._

import reactivemongo.bson.BSONObjectID

import models.User

trait AuthenticatedController[T] extends ReactiveMongoAutoSourceController[T] with Authentication {
  def collectionName: String
  val coll = db.collection[JSONCollection](collectionName)

  override def insert = Authenticated { _ =>
    super.insert
  }
  override def get(id: BSONObjectID) = Authenticated { _ =>
    super.get(id)
  }
  override def delete(id: BSONObjectID) = Authenticated { _ =>
    super.delete(id)
  }
  override def update(id: BSONObjectID) = Authenticated { _ =>
    super.update(id)
  }
  override def updatePartial(id: BSONObjectID) = Authenticated { _ =>
    super.updatePartial(id)
  }
  override def find = Authenticated { _ =>
    super.find
  }
  override def findStream = Authenticated { _ =>
    super.findStream
  }
  override def batchInsert = Authenticated { _ =>
    super.batchInsert
  }
  override def batchDelete = Authenticated { _ =>
    super.batchDelete
  }
  override def batchUpdate = Authenticated { _ =>
    super.batchUpdate
  }

}
