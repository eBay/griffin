/*
 * Copyright (c) 2016 eBay Software Foundation. Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 */

package org.apache.bark.repo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.bark.domain.DataAsset;
import org.apache.bark.domain.DqModel;
import org.apache.bark.domain.ModelType;
import org.apache.bark.domain.ValidityType;
import org.apache.bark.vo.MappingItemInput;
import org.apache.bark.vo.ModelInput;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Repository
public class DqModelRepoImpl extends BaseIdRepo<DqModel> implements DqModelRepo {

    public DqModelRepoImpl() {
        super("dq_model", "DQ_MODEL_NO", DqModel.class);
    }

    @Override
    public List<DqModel> getByStatus(int status) {
        List<DqModel> result = new ArrayList<DqModel>();
        DBCursor cursor = dbCollection.find(new BasicDBObject("status", status));
        for (DBObject dbo : cursor) {
            result.add(toEntity(dbo));
        }
        return result;
    }

    @Override
    public void update(DqModel entity) {
        DBObject temp = dbCollection.findOne(new BasicDBObject("modelId", entity.getModelName()));
        if (temp != null)
            dbCollection.remove(temp);

        Gson gson = new Gson();
        DBObject t1 = (DBObject) JSON.parse(gson.toJson(entity));
        dbCollection.save(t1);
    }

    // FIXME concerned could be removed
    // get models concerned with data asset
    // allConcerned: false- only the models directly concerned with the data asset
    // true - the models directly concerned and non-directly concerned(eg. as the source asset of
    // accuracy model)
    @Override
    public List<DqModel> getByDataAsset(DataAsset da, boolean allConcerned) {
        List<DqModel> result = new ArrayList<DqModel>();
        List<DqModel> allModels = getAll();
        Iterator<DqModel> itr = allModels.iterator();
        while (itr.hasNext()) {
            DqModel me = itr.next();
            if (me.getAssetId() == da.getId()) { // concerned directly
                result.add(me);
            } else if (allConcerned) { // check the non-directly concerned models
                if (me.getModelType() == ModelType.ACCURACY) { // accuracy
                    // find model
                    DqModel entity = findByName(me.getModelName());
                    ModelInput mi = new ModelInput();
                    mi.parseFromString(entity.getModelContent());

                    // get mapping list to get the asset name
                    String otherAsset = "";
                    List<MappingItemInput> mappingList = mi.getMappings();
                    Iterator<MappingItemInput> mpitr = mappingList.iterator();
                    while (mpitr.hasNext()) {
                        MappingItemInput mapping = mpitr.next();
                        // since the target data asset is directly concerned, we should get source
                        // as the other one
                        String col = mapping.getSrc();
                        otherAsset = col.replaceFirst("\\..+", ""); // delete from the first .xxxx
                        if (!otherAsset.isEmpty())
                            break;
                    }

                    // check the other asset name equals to this asst or not
                    if (otherAsset.equals(da.getAssetName())) { // concerned non-directly
                        result.add(me);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public DqModel findByColumn(String colName, String value) {

        DBObject temp = dbCollection.findOne(new BasicDBObject(colName, value));

        if (temp == null) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(temp.toString(), DqModel.class);
        }

    }

    @Override
    public DqModel findByName(String name) {

        DBObject temp = dbCollection.findOne(new BasicDBObject("modelName", name));

        if (temp == null) {
            return null;
        } else {
            return new Gson().fromJson(temp.toString(), DqModel.class);
        }

    }

    @Override
    public DBObject findCountModelByAssetID(long assetID) {

        DBCursor cursor = dbCollection.find(new BasicDBObject("assetId", assetID));
        for (DBObject tempDBObject : cursor) {
            if (tempDBObject.get("modelType").equals(ModelType.VALIDITY)) {
                String content = tempDBObject.get("modelContent").toString();
                String[] contents = content.split("\\|");
                if (contents[2].equals(ValidityType.TOTAL_COUNT + "")) {
                    return tempDBObject;
                }
            }
        }

        return null;

    }

    @SuppressWarnings("deprecation")
    @Override
    public void addReference(DBObject old, String reference) {
        dbCollection.remove(old);
        if (old.containsKey("referenceModel")) {
            if (!old.get("referenceModel").equals("")) {
                old.put("referenceModel", old.get("referenceModel") + "," + reference);
            } else {
                old.put("referenceModel", reference);
            }
        }
        dbCollection.save(old);
    }

}
