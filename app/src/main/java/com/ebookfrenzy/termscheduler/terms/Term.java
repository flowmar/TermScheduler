package com.ebookfrenzy.termscheduler.terms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-15 8:33 AM
 * Created By: flowmar
 *
 *
 *******************/
@Entity(tableName = "terms",
        indices = {@Index(value = {"term_name"}, unique = true)}
)
public class Term
    {
        //**********************************************Fields********************************************/

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "term_id")
        private int id;

        @ColumnInfo(name = "term_name")
        private String name;

        @ColumnInfo(name = "start_date")
        private String startDate;

        @ColumnInfo(name = "end_date")
        private String endDate;

        //**********************************************Constructors********************************************/
        public Term(String name, String startDate, String endDate)
            {
                this.name      = name;
                this.startDate = startDate;
                this.endDate   = endDate;
            }

        public Term() {}

        //**********************************************Methods********************************************/

        public int getId()
            {
                return this.id;
            }

        public void setId(@NonNull int id)
            {
                this.id = id;
            }

        public String getName()
            {
                return this.name;
            }

        public void setName(String name)
            {
                this.name = name;
            }

        public String getStartDate()
            {
                return this.startDate;
            }

        public void setStartDate(String startDate)
            {
                this.startDate = startDate;
            }

        public String getEndDate()
            {
                return this.endDate;
            }

        public void setEndDate(String endDate)
            {
                this.endDate = endDate;
            }
    }
