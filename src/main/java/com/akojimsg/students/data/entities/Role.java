package com.akojimsg.students.data.entities;

public enum Role {
  STUDENT ("STUDENT"),
  STAFF ("STAFF"),
  ADMIN ("ADMIN");

  private final String role;
  Role(String role){
    this.role = role;
  }

  @Override
  public String toString() {
    return this.role;
  }
}
